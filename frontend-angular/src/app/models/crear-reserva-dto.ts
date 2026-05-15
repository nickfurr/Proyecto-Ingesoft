export interface CrearReservaDto {
  clienteId: number;
  casaRuralId: number;
  fechaEntrada: string;
  fechaSalida: string;
  modalidad: 'CASA_ENTERA' | 'POR_HABITACIONES' | 'AMBAS';
}