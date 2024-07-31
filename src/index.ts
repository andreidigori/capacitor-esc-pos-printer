import { registerPlugin } from '@capacitor/core';

import type { EscPosPrinterPlugin } from './definitions';

const EscPosPrinter = registerPlugin<EscPosPrinterPlugin>('EscPosPrinter', {
  web: () => import('./web').then(m => new m.EscPosPrinterWeb()),
});

export * from './definitions';
export { EscPosPrinter };
