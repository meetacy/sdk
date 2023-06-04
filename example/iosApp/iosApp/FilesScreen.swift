//
//  FilesScreen.swift
//  iosApp
//
//  Created by Mark Dubkov on 03.06.2023.
//

import SwiftUI
import shared
import Foundation

@MainActor class FilesScreenViewModel: ObservableObject {
    private let useCase: FilesUseCase? = try? iosApp.factory.createFilesUseCase()
    
    @Published var image: UIImage? = nil
    @Published var isLoading: Bool = false
    @Published var error: String? = nil

    func start() {
        subcribe()
    }
    
    func setUrl(_ url: URL) {
        Task { @MainActor in
            await setupImageAsync(url)
        }
    }
    
    
    func setupImageAsync(_ url: URL) async {
        guard let useCase else { return }
        do {
            let fileId = try await useCase.filesApi.upload(source: url, onUpdate: { read, fileSize in
                print("read: \(read), fileSize: \(fileSize)")
            })
            useCase.setupFileId(fileId: fileId)
        } catch {
            self.error = "Upload image error"
        }
    }
    
    private func subcribe() {
        useCase?.subscribe(onChange: { [weak self] apiDownloadableFile in
            guard let self else { return }
            self.download(apiDownloadableFile)
        })
    }
    
    private func download(_ file: DownloadableFile) {
        Task { @MainActor in
            await downloadAsync(file)
        }
    }
    
    private func downloadAsync(_ file: DownloadableFile) async {
        guard let cacheDirectoryURL = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first else {
            self.error = "Unable to access the cache directory."
            return
        }
        
        let temporaryFileName = "\(UUID().uuidString).jpeg"
        let temporaryFileURL = cacheDirectoryURL.appendingPathComponent(temporaryFileName)
        
        if FileManager.default.fileExists(atPath: temporaryFileURL.path()) {
            do {
                try FileManager.default.removeItem(at: temporaryFileURL)
            } catch {
                self.error = "Remove file error"
                return
            }
        }
        
        FileManager.default.createFile(atPath: temporaryFileURL.path(), contents: nil)
        
        do {
            try await file.download(destination: temporaryFileURL) { write, fileSize in
                print("write: \(write), fileSize: \(fileSize)")
            }
            guard let imageData = try? Data(contentsOf: temporaryFileURL) else {
                self.error = "Get image data error"
                return
            }
            
            print(imageData)
            
            guard let image = UIImage(data: imageData) else {
                self.error = "Create image from data error"
                return
            }
            self.image = image
        } catch {
            self.error = "Download file error"
        }
    }
}

struct FilesScreen: View {
    
    @StateObject private var viewModel: FilesScreenViewModel
    
    init() {
        let vm = FilesScreenViewModel()
        _viewModel = StateObject(wrappedValue: vm)
        vm.start()
    }
    
    @State var showImagePicker: Bool = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            if let image = viewModel.image {
                Image(uiImage: image)
                    .resizable()
                    .frame(width: 100, height: 100)
            }
            Button {
                showImagePicker = true
            } label: {
                Text("Show image picker")
            }
            if let errorText = viewModel.error {
                Text(errorText).foregroundColor(.red)
            }
        }
        .sheet(isPresented: $showImagePicker, onDismiss: {
            showImagePicker = false
        }, content: {
            ImagePicker(isShown: $showImagePicker, setUrl: { url in
                viewModel.setUrl(url)
            })
        })
    }
}

struct FilesScreen_Previews: PreviewProvider {
    static var previews: some View {
        FilesScreen()
    }
}


struct ImagePicker: UIViewControllerRepresentable {
    
    @Binding var isShown: Bool
    var setUrl: (URL) -> Void
    
    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        
        @Binding var isShown: Bool
        var setUrl: (URL) -> Void
        
        init(isShown: Binding<Bool>, setUrl: @escaping (URL) -> Void) {
            _isShown = isShown
            self.setUrl = setUrl
        }
        
        func imagePickerController(
            _ picker: UIImagePickerController,
            didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]
        ) {
            if let imageURL = info[UIImagePickerController.InfoKey.imageURL] as? URL {
                setUrl(imageURL)
            }
            isShown = false
        }
        
        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            isShown = false
        }
        
    }
    
    func makeCoordinator() -> Coordinator {
        return Coordinator(isShown: $isShown, setUrl: setUrl)
    }
    
    func makeUIViewController(
        context: UIViewControllerRepresentableContext<ImagePicker>
    ) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(
        _ uiViewController: UIImagePickerController,
        context: UIViewControllerRepresentableContext<ImagePicker>
    ) {
        // nothing
    }
    
}
