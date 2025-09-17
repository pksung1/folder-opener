# Capacitor Folder Opener

A Capacitor plugin that allows you to open folders in the device's file system.

## Install

```bash
npm install pksung1/folder-opener
npx cap sync
```

## API

<docgen-index>

* [`open(...)`](#open)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### open(...)

```typescript
open(options: FolderOpenerOptions) => any
```

Method to open a folder.

| Param         | Type                                                                |
| ------------- | ------------------------------------------------------------------- |
| **`options`** | <code><a href="#folderopeneroptions">FolderOpenerOptions</a></code> |

**Returns:** <code>any</code>

**Since:** 1.0.0

--------------------


### Interfaces


#### FolderOpenerOptions

folder open method options

| Prop                  | Type                                   | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Since |
| --------------------- | -------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`folderPath`**      | <code>string</code>                    | folder path                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | 1.0.0 |
| **`openWithDefault`** | <code>boolean</code>                   | Use the default platform chooser, if true, otherwise: On Android: it will show "Open Folder in.." title of the chooser dialog, the system will always present the chooser dialog even if the user has chosen a default one and if no activity is found to handle the folder, the system will still present a dialog with the specified title and an error message No application can perform this action On iOS: it will presents a menu restricted to a list of apps capable of opening the current folder. This determination is made based on the folder type and on the folder types supported by the installed apps. (optional) default value is true | 1.0.0 |
| **`chooserPosition`** | <code>{ x: number; y: number; }</code> | (iOS only; iPad only) Position to anchor the chooser (ShareSheet) menu in the view (optional) Please note that this is applicable only when the application runs on iPad and when openWithDefault is false, otherwise this is ignored                                                                                                                                                                                                                                                                                                                                                                                                                      | 1.0.0 |

</docgen-api>
