import { registerPlugin } from '@capacitor/core';

import type { EscPosPrinterPlugin } from './definitions';

export const EscPosPrinter = registerPlugin<EscPosPrinterPlugin>('EscPosPrinter', {
  web: () => import('./web').then(m => new m.EscPosPrinterWeb()),
});
