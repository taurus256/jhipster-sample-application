import dayjs from 'dayjs';
import { IDVUser } from 'app/shared/model/dv-user.model';
import { OS } from 'app/shared/model/enumerations/os.model';
import { BROWSER } from 'app/shared/model/enumerations/browser.model';

export interface IDVTask {
  id?: number;
  uuid?: string | null;
  createTime?: string | null;
  retryCounter?: number | null;
  errorDesc?: string | null;
  operationSystem?: OS | null;
  browser?: BROWSER | null;
  resolutionWidth?: number | null;
  resolutionHeight?: number | null;
  user?: IDVUser | null;
}

export const defaultValue: Readonly<IDVTask> = {};
