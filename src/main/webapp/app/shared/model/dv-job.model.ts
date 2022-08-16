import dayjs from 'dayjs';
import { IDVTask } from 'app/shared/model/dv-task.model';
import { OS } from 'app/shared/model/enumerations/os.model';
import { BROWSER } from 'app/shared/model/enumerations/browser.model';

export interface IDVJob {
  id?: number;
  uuid?: string | null;
  createTime?: string | null;
  isError?: boolean | null;
  isSuccess?: boolean | null;
  operationSystem?: OS | null;
  browser?: BROWSER | null;
  task?: IDVTask | null;
}

export const defaultValue: Readonly<IDVJob> = {
  isError: false,
  isSuccess: false,
};
