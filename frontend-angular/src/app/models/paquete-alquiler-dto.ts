export interface PaqueteAlquilerDto {
  casaRuralId: number;
  fechaInicio: string;
  fechaFin: string;
  modalidad: string;
  precioCasaEntera?: number | null;
  precioPorHabitacion?: number | null;
}
