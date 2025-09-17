package com.pksung1.folder.opener;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@CapacitorPlugin(name = "FolderOpener")
public class FolderOpenerPlugin extends Plugin {
    private static final String TAG = "FolderOpenerPlugin";

    @PluginMethod
    public void open(PluginCall call) {
        String filePath = call.getString("filePath");

        if (filePath == null) {
            call.reject("File path is required");
            return;
        }

        // URL 디코드 처리
        String decodedFilePath;
        try {
            decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            Log.w(TAG, "Failed to decode file path, using original: " + e.getMessage());
            decodedFilePath = filePath;
        }

        try {
            File file = new File(decodedFilePath);
            if (!file.exists()) {
                call.reject("File does not exist");
                return;
            }

            // Get the parent folder
            File folder = file.getParentFile();
            if (folder == null || !folder.exists()) {
                call.reject("Parent folder does not exist");
                return;
            }

            // Try to open with specific file manager apps first
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri folderUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // For Android 7.0+ (API level 24+), we need to use FileProvider
                folderUri = FileProvider.getUriForFile(
                    getContext(),
                    getContext().getPackageName() + ".fileprovider",
                    folder
                );
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                folderUri = Uri.fromFile(folder);
            }

            intent.setDataAndType(folderUri, "resource/folder");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            // Try to open with specific file manager apps
            String[] fileManagerPackages = {
                "com.android.documentsui", // Android Files app
                "com.google.android.documentsui", // Google Files app
                "com.samsung.android.app.myfiles", // Samsung My Files
                "com.sec.android.app.myfiles", // Samsung My Files (older)
                "com.mi.android.globalFileexplorer", // MI File Manager
                "com.oneplus.filemanager", // OnePlus File Manager
                "com.huawei.filemanager", // Huawei File Manager
                "com.lenovo.FileManager", // Lenovo File Manager
                "com.oppo.filemanager", // OPPO File Manager
                "com.vivo.filemanager" // Vivo File Manager
            };
            
            boolean opened = false;
            for (String packageName : fileManagerPackages) {
                Intent specificIntent = new Intent(Intent.ACTION_VIEW);
                specificIntent.setDataAndType(folderUri, "resource/folder");
                specificIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                specificIntent.setPackage(packageName);
                
                try {
                    getContext().startActivity(specificIntent);
                    opened = true;
                    break;
                } catch (Exception e) {
                    // This file manager is not available, try the next one
                    Log.d(TAG, "File manager not available: " + packageName);
                }
            }
            
            // If no specific file manager worked, fall back to chooser
            if (!opened) {
                Intent chooser = Intent.createChooser(intent, "Open Folder with");
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(chooser);
            }

            call.resolve();
        } catch (Exception e) {
            Log.e(TAG, "Error opening folder", e);
            call.reject("Error opening folder: " + e.getMessage(), e);
        }
    }
}
