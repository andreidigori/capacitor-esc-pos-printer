import type { PrinterErrorCode } from '../enums';

export class PrinterError extends Error {
  constructor(
    message: string,
    public readonly errorCode: PrinterErrorCode,
  ) {
    super(message);
  }
}
  