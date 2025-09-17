import Foundation
import Capacitor
import UIKit

/**
 * Capacitor plugin for opening folders in the file system
 */
@objc(FolderOpenerPlugin)
public class FolderOpenerPlugin: CAPPlugin {
    
    @objc func open(_ call: CAPPluginCall) {
        guard let filePath = call.getString("filePath") else {
            call.reject("File path is required")
            return
        }
        
        let fileManager = FileManager.default
        
        // Check if the file exists
        if !fileManager.fileExists(atPath: filePath) {
            call.reject("File does not exist")
            return
        }
        
        // Get the folder containing the file
        let fileURL = URL(fileURLWithPath: filePath)
        let folderURL = fileURL.deletingLastPathComponent()
        
        DispatchQueue.main.async {
            if #available(iOS 13.0, *) {
                // Files 앱을 열고 해당 폴더로 이동
                let filesAppURL = URL(string: "shareddocuments://\(folderURL.path)")!
                
                if UIApplication.shared.canOpenURL(filesAppURL) {
                    UIApplication.shared.open(filesAppURL, options: [:]) { success in
                        if success {
                            call.resolve()
                        } else {
                            call.reject("Could not open folder in Files app")
                        }
                    }
                } else {
                    // Files 앱이 없는 경우 UIDocumentInteractionController 사용
                    let documentController = UIDocumentInteractionController(url: folderURL)
                    documentController.delegate = self
                    
                    if let viewController = self.bridge?.viewController,
                       let sourceView = viewController.view {
                        
                        let rect = CGRect(x: sourceView.bounds.midX, y: sourceView.bounds.midY, width: 1, height: 1)
                        let presented = documentController.presentOptionsMenu(from: rect, in: sourceView, animated: true)
                        
                        if presented {
                            call.resolve()
                        } else {
                            call.reject("Could not present folder options")
                        }
                    } else {
                        call.reject("Could not access view controller")
                    }
                }
            } else {
                // For older iOS versions, use UIDocumentInteractionController
                let documentController = UIDocumentInteractionController(url: folderURL)
                documentController.delegate = self
                
                if let viewController = self.bridge?.viewController,
                   let sourceView = viewController.view {
                    
                    let rect = CGRect(x: sourceView.bounds.midX, y: sourceView.bounds.midY, width: 1, height: 1)
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
