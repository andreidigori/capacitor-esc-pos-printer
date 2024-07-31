import { EscPosPrinter } from 'capacitor-esc-pos-printer';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    EscPosPrinter.echo({ value: inputValue })
}
