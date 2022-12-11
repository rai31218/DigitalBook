export class Book{
	public id?:number;
    public logo?:File;
	public title :string;
	public category:string;
	public price : number;
	public authorId?: number;
	public publisher:string;
	public publishedDate?:string;
	public active:boolean;
    public content:string;
}