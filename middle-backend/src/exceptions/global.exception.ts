import { HttpException } from '@nestjs/common';

export class GlobalException extends HttpException {
  constructor(message: string, statusCode: number) {
    super(message, statusCode);
  }
}
