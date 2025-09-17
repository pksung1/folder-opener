// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "Pksung1FolderOpener",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "Pksung1FolderOpener",
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
