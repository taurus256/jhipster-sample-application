export interface IDVUser {
  id?: number;
  name?: string | null;
  email?: string | null;
}

export const defaultValue: Readonly<IDVUser> = {};
