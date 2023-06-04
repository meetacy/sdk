//
//  iosAppApp.swift
//  iosApp
//
//  Created by Mark Dubkov on 03.06.2023.
//

import SwiftUI
import shared

enum Routes {
    case register
    case files
}

@main
struct iosApp: App {
    
    static var factory: SharedFactory!
    
    init() {
        iosApp.factory = SharedFactory()
    }
    
    var body: some Scene {
        WindowGroup {
            NavigationStack {
                VStack(spacing: 20) {
                    NavigationLink("Auth", value: Routes.register)
                    NavigationLink("Files", value: Routes.files)
                }
                .navigationDestination(
                    for: Routes.self,
                    destination: { routes in
                        switch routes {
                        case .files:
                            FilesScreen()
                        case .register:
                            AuthScreen()
                        }
                    }
                )
            }
        }
    }
}

private struct MainScreen: View {
    
    var body: some View {
        VStack {
            NavigationLink("Auth", value: Routes.files)
            NavigationLink("Auth", value: Routes.files)
        }
    }
}

