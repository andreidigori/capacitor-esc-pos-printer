import { PrinterError } from '../errors/printer-error';
import { EscPosPrinter } from '../plugin';

import { CapacitorLinkedPrinter } from './capacitor-linked-printer';

export abstract class BasePrinter extends CapacitorLinkedPrinter {
  protected writeBuffer: number[] | undefined;

  async isConnected(): Promise<boolean> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    if (!this.writeBuffer) {
      return false;
    }

    const { value } = await EscPosPrinter.isPrinterConnected({
      hashKey: this.internalHashKey,
    });
    return value;
  }

  async connect(): Promise<void> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    try {
      await EscPosPrinter.connectPrinter({
        hashKey: this.internalHashKey,
      });

      this.writeBuffer = [];
    } catch (e) {
      throw this.parseError(e);
    }
  }

  async disconnect(): Promise<void> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    await EscPosPrinter.disconnectPrinter({
      hashKey: this.internalHashKey,
    });

    this.writeBuffer = undefined;
  }

  async write(data: number[]): Promise<void> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    if (!this.writeBuffer) {
      throw new Error('Printer not connected');
    }

    const clampedBytes = new Uint8ClampedArray(data);
    this.writeBuffer.push(...clampedBytes);
  }

  async send(waitingTime?: number): Promise<void>;
  async send(data: number[]): Promise<void>;
  async send(data: number[], waitingTime: number): Promise<void>;
  async send(timeOrData?: number | number[], waitingTime?: number): Promise<void> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    if (!this.writeBuffer) {
      throw new Error('Printer not connected');
    }

    if (Array.isArray(timeOrData)) {
      const clampedBytes = new Uint8ClampedArray(timeOrData);
      this.writeBuffer.push(...clampedBytes);
    } else if (typeof timeOrData === 'number') {
      waitingTime = timeOrData;
    }

    try {
      await EscPosPrinter.sendToPrinter({
        hashKey: this.internalHashKey,
        data: this.writeBuffer,
        waitingTime,
      });
  
      this.writeBuffer = [];
    } catch (e) {
      throw this.parseError(e);
    }
  }

  async read(): Promise<number[]> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    try {
      const { value } = await EscPosPrinter.readFromPrinter({
        hashKey: this.internalHashKey,
      });
      return value;
    } catch (e) {
      throw this.parseError(e);
    }
  }

  protected parseError(e: any): any {
    if (!e.data) {
      return e;
    }
  
    return new PrinterError(e.message, e.data.code);
  }
}
