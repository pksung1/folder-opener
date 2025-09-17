// Example usage of the folder-opener plugin
/* eslint-disable no-console */

import { FolderOpener } from 'folder-opener';

// Function to open a folder containing a file
async function openFolderContainingFile(filePath) {
  try {
    await FolderOpener.open({
      filePath: filePath
    });
    console.log('Folder opened successfully');
  } catch (error) {
    console.error('Error opening folder:', error);
  }
}

// Example usage
// Replace with an actual file path on your device
const filePath = '/path/to/your/file.txt';
openFolderContainingFile(filePath);

// For Android, you might use something like:
// const filePath = '/storage/emulated/0/Download/document.pdf';

// For iOS, you might use something like:
// const filePath = '/var/mobile/Containers/Data/Application/.../document.pdf';
