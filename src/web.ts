import { WebPlugin } from '@capacitor/core';

import type { EscPosPrinterPlugin } from './definitions';

export class EscPosPrinterWeb extends WebPlugin implements EscPosPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
