import { User } from "./user.model";

export class Subscription {
    public id:number;
    public user:User;
     public bookId:number;
     public dateOfSubscription:Date;
     public isCancelled:boolean;
     public dateOfCancellation:Date;

}