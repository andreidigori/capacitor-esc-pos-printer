import type { CreatePrinterOptions } from '../definitions';
import { EscPosPrinter } from '../plugin';

export abstract class CapacitorLinkedPrinter {
  protected internalHashKey: string | undefined;

  constructor(
    protected createOptions: CreatePrinterOptions,
  ) {}

  isLinked(): boolean {
    return !!this.internalHashKey;
  }
  
  async link(): Promise<void> {
    if (this.internalHashKey) {
      // Prevent recreating new control in native hash map
      return;
    }

    const { value } = await EscPosPrinter.createPrinter(this.createOptions);
    this.internalHashKey = value;
  }
  
  async dispose(): Promise<void> {
    if (!this.internalHashKey) {
      throw new Error('Printer not linked to native');
    }

    const { value } = await EscPosPrinter.disposePrinter({
      hashKey: this.internalHashKey,
    });

    if (!value) {
      throw new Error('Printer cannot be disposed');
    }

    this.internalHashKey = undefined;
  }
}
