export enum TipoCama {
  INDIVIDUAL = 'INDIVIDUAL',
  DOBLE = 'DOBLE',
  QUEEN = 'QUEEN',
  KING = 'KING'
}

export interface HabitacionDto {
  codigo: number;
  numeroCamas: number;
  tipoCama: TipoCama;
  tieneBano: boolean;
}
