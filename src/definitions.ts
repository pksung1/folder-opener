/**
 * folder open method options
 *
 * @since 1.0.0
 */
export interface FolderOpenerOptions {
  /**
   * file path - the plugin will open the folder containing this file
   *
   * @since 1.0.0
   */
  filePath: string;
}

export interface FolderOpenerPlugin {
  /**
   * Method to open the folder containing the specified file.
   *
   * @since 1.0.0
   */
  open(options: FolderOpenerOptions): Promise<void>;
}
