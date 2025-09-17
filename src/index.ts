import { registerPlugin } from '@capacitor/core';

import type { FolderOpenerPlugin } from './definitions.js';

const FolderOpener = registerPlugin<FolderOpenerPlugin>('FolderOpener');

export * from './definitions.js';
export { FolderOpener };
