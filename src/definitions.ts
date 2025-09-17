/**
 * folder open method options
 *
 * @since 1.0.0
 */
export interface FolderOpenerOptions {
  /**
   * folder path
   *
   * @since 1.0.0
   */
  folderPath: string;
  /**
   * Use the default platform chooser, if true, otherwise:
   * On Android: it will show "Open Folder in.." title of the chooser dialog, the system will always present the chooser dialog
   * even if the user has chosen a default one and if no activity is found to handle the folder, the system will still
   * present a dialog with the specified title and an error message No application can perform this action
   * On iOS: it will presents a menu restricted to a list of apps capable of opening the current folder.
   * This determination is made based on the folder type and on the folder types supported by the installed apps.
   *
   * (optional) default value is true
   *
   * @since 1.0.0
   */
  openWithDefault?: boolean;
  /**
   * (iOS only; iPad only) Position to anchor the chooser (ShareSheet) menu in the view (optional)
   * Please note that this is applicable only when the application runs on iPad and when
   * openWithDefault is false, otherwise this is ignored
   *
   * @since 1.0.0
   */
   chooserPosition?: {
    x: number;
    y: number;
  };
}

export interface FolderOpenerPlugin {
  /**
   * Method to open a folder.
   *
   * @since 1.0.0
   */
  open(options: FolderOpenerOptions): Promise<void>;
}
