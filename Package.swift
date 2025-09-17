// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FolderOpener",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "FolderOpener",
            targets: ["FolderOpenerPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift.git", branch: "main")
    ],
    targets: [
        .target(
            name: "FolderOpenerPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift"),
            ],
            path: "ios/Plugin")
    ]
)
