# capacitor-esc-pos-printer

Esc Pos

## Install

```bash
npm install capacitor-esc-pos-printer
npx cap sync
```

## API

<docgen-index>

* [`requestBluetoothEnable()`](#requestbluetoothenable)
* [`getBluetoothPrinterDevices()`](#getbluetoothprinterdevices)
* [`createPrinter(...)`](#createprinter)
* [`disposePrinter(...)`](#disposeprinter)
* [`isPrinterConnected(...)`](#isprinterconnected)
* [`connectPrinter(...)`](#connectprinter)
* [`disconnectPrinter(...)`](#disconnectprinter)
* [`sendToPrinter(...)`](#sendtoprinter)
* [`readFromPrinter(...)`](#readfromprinter)
* [Interfaces](#interfaces)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestBluetoothEnable()

```typescript
requestBluetoothEnable() => Promise<ValueResult<boolean>>
```

**Returns:** <code>Promise&lt;<a href="#valueresult">ValueResult</a>&lt;boolean&gt;&gt;</code>

--------------------


### getBluetoothPrinterDevices()

```typescript
getBluetoothPrinterDevices() => Promise<BluetoothDevicesResult>
```

**Returns:** <code>Promise&lt;<a href="#bluetoothdevicesresult">BluetoothDevicesResult</a>&gt;</code>

--------------------


### createPrinter(...)

```typescript
createPrinter(options: CreatePrinterOptions) => Promise<ValueResult<string>>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#createprinteroptions">CreatePrinterOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#valueresult">ValueResult</a>&lt;string&gt;&gt;</code>

--------------------


### disposePrinter(...)

```typescript
disposePrinter(options: WithHashKey) => Promise<ValueResult<boolean>>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#withhashkey">WithHashKey</a></code> |

**Returns:** <code>Promise&lt;<a href="#valueresult">ValueResult</a>&lt;boolean&gt;&gt;</code>

--------------------


### isPrinterConnected(...)

```typescript
isPrinterConnected(options: WithHashKey) => Promise<ValueResult<boolean>>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#withhashkey">WithHashKey</a></code> |

**Returns:** <code>Promise&lt;<a href="#valueresult">ValueResult</a>&lt;boolean&gt;&gt;</code>

--------------------


### connectPrinter(...)

```typescript
connectPrinter(options: WithHashKey) => Promise<void>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#withhashkey">WithHashKey</a></code> |

--------------------


### disconnectPrinter(...)

```typescript
disconnectPrinter(options: WithHashKey) => Promise<void>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#withhashkey">WithHashKey</a></code> |

--------------------


### sendToPrinter(...)

```typescript
sendToPrinter(options: SendToPrinterOptions) => Promise<void>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#sendtoprinteroptions">SendToPrinterOptions</a></code> |

--------------------


### readFromPrinter(...)

```typescript
readFromPrinter(options: WithHashKey) => Promise<ValueResult<number[]>>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#withhashkey">WithHashKey</a></code> |

**Returns:** <code>Promise&lt;<a href="#valueresult">ValueResult</a>&lt;number[]&gt;&gt;</code>

--------------------


### Interfaces


#### ValueResult

| Prop        | Type           |
| ----------- | -------------- |
| **`value`** | <code>T</code> |


#### BluetoothDevicesResult

| Prop          | Type                                                                                                                |
| ------------- | ------------------------------------------------------------------------------------------------------------------- |
| **`devices`** | <code>{ address: string; alias?: string; name: string; bondState: number; type: number; uuids: string[]; }[]</code> |


#### CreatePrinterOptions

| Prop                 | Type                                                                    |
| -------------------- | ----------------------------------------------------------------------- |
| **`connectionType`** | <code><a href="#printerconnectiontype">PrinterConnectionType</a></code> |
| **`address`**        | <code>string</code>                                                     |


#### WithHashKey

| Prop          | Type                |
| ------------- | ------------------- |
| **`hashKey`** | <code>string</code> |


#### SendToPrinterOptions

| Prop              | Type                  |
| ----------------- | --------------------- |
| **`data`**        | <code>number[]</code> |
| **`waitingTime`** | <code>number</code>   |


### Enums


#### PrinterConnectionType

| Members         | Value                    |
| --------------- | ------------------------ |
| **`Bluetooth`** | <code>'bluetooth'</code> |

</docgen-api>
