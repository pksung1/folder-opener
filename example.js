// Example usage of the folder-opener plugin

import { FolderOpener } from 'folder-opener';

// Function to open a folder
async function openFolder(folderPath) {
  try {
    await FolderOpener.open({
      folderPath: folderPath,
      // Optional parameters
      openWithDefault: true,
      // chooserPosition: { x: 100, y: 100 } // iOS only, iPad only
    });
    console.log('Folder opened successfully');
  } catch (error) {
    console.error('Error opening folder:', error);
  }
}

// Example usage
// Replace with an actual folder path on your device
const folderPath = '/path/to/your/folder';
openFolder(folderPath);

// For Android, you might use something like:
// const folderPath = '/storage/emulated/0/Download';

// For iOS, you might use something like:
// const folderPath = '/var/mobile/Containers/Data/Application/...';
