import type { PrinterConnectionType } from './enums/printer-connection-type';

/* Utils */

export interface WithHashKey {
  hashKey: string;
}

/* Results */

export interface ValueResult<T> {
  value: T;
}

export interface BluetoothDevicesResult {
  devices: ({
    address: string;
    alias?: string;
    name: string;
    bondState: number;
    type: number;
    uuids: string[];
  })[];
}

/* Options */

export interface CreatePrinterOptions {
  connectionType: PrinterConnectionType;
  address: string;
  [key: string]: any;
}

export interface SendToPrinterOptions extends WithHashKey {
  data: number[];
  waitingTime?: number;
}

/* Plugin */

export interface EscPosPrinterPlugin {
  requestBluetoothEnable(): Promise<ValueResult<boolean>>;
  getBluetoothPrinterDevices(): Promise<BluetoothDevicesResult>;
  createPrinter(options: CreatePrinterOptions): Promise<ValueResult<string>>;
  disposePrinter(options: WithHashKey): Promise<ValueResult<boolean>>;
  isPrinterConnected(options: WithHashKey): Promise<ValueResult<boolean>>;
  connectPrinter(options: WithHashKey): Promise<void>;
  disconnectPrinter(options: WithHashKey): Promise<void>;
  sendToPrinter(options: SendToPrinterOptions): Promise<void>;
  readFromPrinter(options: WithHashKey): Promise<ValueResult<number[]>>;
}
