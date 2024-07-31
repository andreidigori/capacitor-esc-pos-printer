export interface EscPosPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
