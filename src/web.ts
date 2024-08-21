import { WebPlugin } from '@capacitor/core';

import type { CreatePrinterOptions, EscPosPrinterPlugin, ValueResult, WithHashKey, SendToPrinterOptions, BluetoothDevicesResult } from './definitions';

export class EscPosPrinterWeb extends WebPlugin implements EscPosPrinterPlugin {
  async requestBluetoothEnable(): Promise<ValueResult<boolean>>{
    return { value: false };
  }

  async getBluetoothPrinterDevices(): Promise<BluetoothDevicesResult> {
    const devices = await navigator.bluetooth.getDevices();
    console.log('getBluetoothPrinterDevices', devices);
    return { devices: [] };
  }

  async createPrinter(options: CreatePrinterOptions): Promise<ValueResult<string>> {
    console.log('createPrinter', JSON.stringify(options));
    return { value: '' };
  }

  async disposePrinter(options: WithHashKey): Promise<ValueResult<boolean>> {
    console.log('disposePrinter', JSON.stringify(options));
    return { value: false };
  }

  async isPrinterConnected(options: WithHashKey): Promise<ValueResult<boolean>> {
    console.log('isPrinterConnected', JSON.stringify(options));
    return { value: false };
  }

  async connectPrinter(options: WithHashKey): Promise<void> {
    console.log('connectPrinter', JSON.stringify(options));
  }

  async disconnectPrinter(options: WithHashKey): Promise<void> {
    console.log('disconnectPrinter', JSON.stringify(options));
  }

  async sendToPrinter(options: SendToPrinterOptions): Promise<void> {
    console.log('sendToPrinter', JSON.stringify(options));
  }

  async readFromPrinter(options: WithHashKey): Promise<ValueResult<number[]>> {
    console.log('readFromPrinter', JSON.stringify(options));
    return { value: [] };
  }
}
