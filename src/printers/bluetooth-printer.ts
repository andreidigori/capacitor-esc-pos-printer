import { PrinterConnectionType } from '../enums/printer-connection-type';

import { BasePrinter } from './base-printer';

export class BluetoothPrinter extends BasePrinter {
  constructor(address: string, secure?: boolean) {
    super({
      connectionType: PrinterConnectionType.Bluetooth,
      address,
      secure,
    });
  }
}
