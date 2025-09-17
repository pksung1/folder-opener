import Foundation
import Capacitor

/**
 * Capacitor plugin for opening folders in the file system
 */
@objc(FolderOpenerPlugin)
public class FolderOpenerPlugin: CAPPlugin {
    
    @objc func open(_ call: CAPPluginCall) {
        guard let folderPath = call.getString("folderPath") else {
            call.reject("Folder path is required")
            return
        }
        
        let openWithDefault = call.getBool("openWithDefault", true)
        
        let fileManager = FileManager.default
        
        // Check if the folder exists
        var isDir: ObjCBool = false
        if !fileManager.fileExists(atPath: folderPath, isDirectory: &isDir) {
            call.reject("Folder does not exist")
            return
        }
        
        if !isDir.boolValue {
            call.reject("Path is not a directory")
            return
        }
        
        let folderURL = URL(fileURLWithPath: folderPath)
        
        DispatchQueue.main.async {
            if #available(iOS 13.0, *) {
                // For iOS 13 and later, we can use UIApplication.shared.open
                let activityVC = UIActivityViewController(activityItems: [folderURL], applicationActivities: nil)
                
                if let chooserPosition = call.getObject("chooserPosition") as? [String: Any],
                   let x = chooserPosition["x"] as? Double,
                   let y = chooserPosition["y"] as? Double,
                   let viewController = self.bridge?.viewController,
                   let sourceView = viewController.view,
                   !openWithDefault,
                   UIDevice.current.userInterfaceIdiom == .pad {
                    
                    activityVC.popoverPresentationController?.sourceView = sourceView
                    activityVC.popoverPresentationController?.sourceRect = CGRect(x: x, y: y, width: 1, height: 1)
                }
                
                self.bridge?.viewController?.present(activityVC, animated: true, completion: nil)
                call.resolve()
            } else {
                // For older iOS versions, use UIDocumentInteractionController
                let documentController = UIDocumentInteractionController(url: folderURL)
                documentController.delegate = self
                
                if let chooserPosition = call.getObject("chooserPosition") as? [String: Any],
                   let x = chooserPosition["x"] as? Double,
                   let y = chooserPosition["y"] as? Double,
                   let viewController = self.bridge?.viewController,
                   let sourceView = viewController.view,
                   !openWithDefault,
                   UIDevice.current.userInterfaceIdiom == .pad {
                    
                    let rect = CGRect(x: x, y: y, width: 1, height: 1)
                    let presented = documentController.presentOptionsMenu(from: rect, in: sourceView, animated: true)
                    
                    if presented {
                        call.resolve()
                    } else {
                        call.reject("Could not present folder options")
                    }
                } else {
                    let presented = documentController.presentPreview(animated: true)
                    
                    if presented {
                        call.resolve()
                    } else {
                        call.reject("Could not present folder preview")
                    }
                }
            }
        }
    }
}

// MARK: - UIDocumentInteractionControllerDelegate
extension FolderOpenerPlugin: UIDocumentInteractionControllerDelegate {
    public func documentInteractionControllerViewControllerForPreview(_ controller: UIDocumentInteractionController) -> UIViewController {
        return self.bridge?.viewController ?? UIViewController()
    }
}
