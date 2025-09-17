# Capacitor Folder Opener

A Capacitor plugin that allows you to open folders containing a specified file in the device's file system.

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

Method to open the folder containing the specified file.

| Param         | Type                                                                |
| ------------- | ------------------------------------------------------------------- |
| **`options`** | <code><a href="#folderopeneroptions">FolderOpenerOptions</a></code> |

**Returns:** <code>any</code>

**Since:** 1.0.0

--------------------


### Interfaces


#### FolderOpenerOptions

folder open method options

| Prop           | Type                | Description                                                      | Since |
| -------------- | ------------------- | ---------------------------------------------------------------- | ----- |
| **`filePath`** | <code>string</code> | file path - the plugin will open the folder containing this file | 1.0.0 |

</docgen-api>
