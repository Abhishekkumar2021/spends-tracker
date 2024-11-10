export class CustomResponse {
  constructor(
    public message: string,
    public statusCode: number,
    public data?: object,
  ) {}
}
