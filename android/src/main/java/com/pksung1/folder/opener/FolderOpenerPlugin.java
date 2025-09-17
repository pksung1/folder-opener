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

        // file:// URI 처리
        String actualFilePath;
        try {
            if (filePath.startsWith("file://")) {
                // file:// URI에서 실제 경로 추출
                Uri uri = Uri.parse(filePath);
                actualFilePath = uri.getPath();
                if (actualFilePath == null) {
                    actualFilePath = filePath.substring(7); // "file://" 제거
                }
            } else {
                actualFilePath = filePath;
            }
            
            // URL 디코드 처리
            actualFilePath = URLDecoder.decode(actualFilePath, StandardCharsets.UTF_8.toString());
            Log.d(TAG, "Original path: " + filePath);
            Log.d(TAG, "Processed path: " + actualFilePath);
        } catch (Exception e) {
            Log.w(TAG, "Failed to process file path, using original: " + e.getMessage());
            actualFilePath = filePath;
        }

        try {
            File file = new File(actualFilePath);
            Log.d(TAG, "File exists check: " + file.getAbsolutePath() + " - " + file.exists());
            if (!file.exists()) {
                call.reject("File does not exist at path: " + actualFilePath);
                return;
            }

            // Get the parent folder
            File folder = file.getParentFile();
            if (folder == null || !folder.exists()) {
                call.reject("Parent folder does not exist");
                return;
            }

            // 폴더 열기 방법 1: 파일 매니저 앱으로 폴더 열기
            boolean opened = false;
            
            // 먼저 일반적인 파일 매니저 앱들로 시도
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
            
            for (String packageName : fileManagerPackages) {
                try {
                    Intent fileManagerIntent = new Intent(Intent.ACTION_VIEW);
                    fileManagerIntent.setDataAndType(Uri.fromFile(folder), "resource/folder");
                    fileManagerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    fileManagerIntent.setPackage(packageName);
                    
                    getContext().startActivity(fileManagerIntent);
                    opened = true;
                    Log.d(TAG, "Opened with file manager: " + packageName);
                    break;
                } catch (Exception e) {
                    Log.d(TAG, "File manager not available: " + packageName);
                }
            }
            
            // 방법 2: DocumentsUI로 폴더 열기 시도
            if (!opened) {
                try {
                    Intent documentsIntent = new Intent(Intent.ACTION_VIEW);
                    documentsIntent.setDataAndType(Uri.fromFile(folder), "resource/folder");
                    documentsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    documentsIntent.setPackage("com.android.documentsui");
                    getContext().startActivity(documentsIntent);
                    opened = true;
                    Log.d(TAG, "Opened with DocumentsUI");
                } catch (Exception e) {
                    Log.d(TAG, "DocumentsUI not available");
                }
            }
            
            // 방법 3: 일반적인 폴더 열기 시도
            if (!opened) {
                try {
                    Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    folderIntent.setDataAndType(Uri.fromFile(folder), "resource/folder");
                    folderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    
                    Intent chooser = Intent.createChooser(folderIntent, "폴더 열기");
                    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(chooser);
                    opened = true;
                    Log.d(TAG, "Opened with chooser");
                } catch (Exception e) {
                    Log.d(TAG, "Chooser failed: " + e.getMessage());
                }
            }
            
            // 방법 4: 파일 경로를 클립보드에 복사하고 알림 표시
            if (!opened) {
                try {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("폴더 경로", folder.getAbsolutePath());
                    clipboard.setPrimaryClip(clip);
                    
                    Log.d(TAG, "Folder path copied to clipboard: " + folder.getAbsolutePath());
                    call.resolve(new JSObject().put("message", "폴더 경로가 클립보드에 복사되었습니다: " + folder.getAbsolutePath()));
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "Failed to copy to clipboard: " + e.getMessage());
                }
            }

            call.resolve();
        } catch (Exception e) {
            Log.e(TAG, "Error opening folder", e);
            call.reject("Error opening folder: " + e.getMessage(), e);
        }
    }
}
