import { HttpStatus, Injectable, NestMiddleware } from '@nestjs/common';
import { NextFunction, Request } from 'express';
import { GlobalException } from 'src/exceptions/global.exception';

@Injectable()
export class CookieMiddleware implements NestMiddleware {
  use(req: Request, _res: Response, next: NextFunction) {
    // extract the cookies from the request
    const { cookies } = req;

    if (req.path.startsWith('/api/auth')) {
      next();
      return;
    }

    // check if the access token & refresh token cookies are present
    if (!cookies.accessToken || !cookies.refreshToken) {
      throw new GlobalException(
        'The access token or refresh token is missing',
        HttpStatus.UNAUTHORIZED,
      );
    }

    next();
  }
}
