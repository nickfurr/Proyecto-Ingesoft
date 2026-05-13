import { CocinaDto } from './cocina-dto';
import { HabitacionDto } from './habitacion-dto';
import { PropietarioSimpleDto } from './propietario-simple-dto';
import { BanoDto } from './baño-dto';

export interface CasaDetallDto {
  codigo: number;
  plazasParqueo: number;
  descripcion: string;
  propietario: PropietarioSimpleDto;
  cocinas: CocinaDto[];
  habitaciones: HabitacionDto[];
  banos: BanoDto[];
  fotos?: string[];
}
