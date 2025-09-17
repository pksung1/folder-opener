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

        try {
            File file = new File(filePath);
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
            
            // Create a chooser to let the user select which app to use
            Intent chooser = Intent.createChooser(intent, "Open Folder with");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(chooser);

            call.resolve();
        } catch (Exception e) {
            Log.e(TAG, "Error opening folder", e);
            call.reject("Error opening folder: " + e.getMessage(), e);
        }
    }
}
