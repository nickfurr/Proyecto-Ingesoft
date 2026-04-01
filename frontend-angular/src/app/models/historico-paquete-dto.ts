export interface HistoricoPaqueteDto {
  idPaquete: number;
  casaRuralId: number;
  fechaInicio: string;
  fechaFin: string;
  modalidad: string;
  precioCasaEntera: number;
  precioPorHabitacion: number;
  vigente: boolean;
  totalReservas: number;
  ingresos: number;
}
