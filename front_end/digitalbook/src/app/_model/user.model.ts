import { Role } from "./role.model"

export class User{
    id: number;
    userName:string;
    password: string;
    email: string;
    createdDate:Date;
    role:Role;

}
