// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorEscPosPrinter",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapacitorEscPosPrinter",
            targets: ["EscPosPrinterPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "EscPosPrinterPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/EscPosPrinterPlugin"),
        .testTarget(
            name: "EscPosPrinterPluginTests",
            dependencies: ["EscPosPrinterPlugin"],
            path: "ios/Tests/EscPosPrinterPluginTests")
    ]
)