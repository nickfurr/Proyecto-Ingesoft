export interface HistoricoPaqueteDto {
  idPaquete: number;
  casaRuralId: number;
  fechaInicio: string;
  fechaFin: string;
  modalidad: string;
  precioCasaEntera: number | null;
  precioPorHabitacion: number | null;
  vigente: boolean;
  totalReservas: number;
  ingresos: number | null;
}
