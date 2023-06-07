//
//  AuthScreen.swift
//  iosApp
//
//  Created by Mark Dubkov on 03.06.2023.
//

import SwiftUI
import shared

@MainActor class AuthScreenViewModel: ObservableObject {
    
    private let useCase = iosApp.factory.createRegisterUseCase()
    
    @Published var isLoading: Bool = false
    @Published var text: String = ""
    @Published var error: String?
    
    func register() {
        Task { @MainActor in
            await registerAsync()
        }
    }
    
    private func registerAsync() async {
        if text.isEmpty { return }
        isLoading = true
        defer {
            isLoading = false
        }
        do {
            try await useCase.register(nickName: text)
        } catch {
            self.error = "Some error"
        }
    }
}

struct AuthScreen: View {
    @StateObject var viewModel: AuthScreenViewModel = .init()
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text("Nickname")
                if viewModel.isLoading {
                    ActivityIndicator()
                }
            }
            TextField("Name", text: $viewModel.text)
            Button {
                viewModel.register()
            } label: {
                Text("Register")
            }

        }
        .padding()
        .navigationTitle("Auth")
    }
}

struct AuthScreen_Previews: PreviewProvider {
    static var previews: some View {
        AuthScreen()
    }
}
