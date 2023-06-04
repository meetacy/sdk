//
//  ActivityIndicator.swift
//  iosApp
//
//  Created by Mark Dubkov on 04.06.2023.
//

import SwiftUI

struct ActivityIndicator: UIViewRepresentable {

    @Binding var isAnimating: Bool
    let style: UIActivityIndicatorView.Style
    
    init(
        isAnimating: Binding<Bool> = .constant(true),
        style: UIActivityIndicatorView.Style = .medium
    ) {
        _isAnimating = isAnimating
        self.style = style
    }

    func makeUIView(context: UIViewRepresentableContext<ActivityIndicator>) -> UIActivityIndicatorView {
        return UIActivityIndicatorView(style: style)
    }

    func updateUIView(_ uiView: UIActivityIndicatorView, context: UIViewRepresentableContext<ActivityIndicator>) {
        isAnimating ? uiView.startAnimating() : uiView.stopAnimating()
    }
}

struct ActivityIndicator_Previews: PreviewProvider {
    static var previews: some View {
        ActivityIndicator(isAnimating: .constant(true), style: .medium)
        ActivityIndicator(isAnimating: .constant(false), style: .medium)
    }
}
