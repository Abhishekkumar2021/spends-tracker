import { Body, Controller, HttpStatus, Post, Req, Res } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Request, Response } from 'express';
import { GlobalException } from 'src/exceptions/global.exception';
import { ApiResponse } from 'src/interfaces/api-response.interface';
import { LoginPayload } from 'src/interfaces/login-payload.interface';
import { AuthService } from 'src/services/auth/auth.service';

@Controller('/api/auth')
export class AuthController {
  constructor(
    private readonly authService: AuthService,
    private readonly configService: ConfigService,
  ) {}

  @Post('/login')
  async login(
    @Body() loginPayload: LoginPayload,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const { username, password } = loginPayload;
    const accessTokenExpiration = this.configService.get(
      'ACCESS_COOKIE_EXPIRATION',
    );
    const refreshTokenExpiration = this.configService.get(
      'REFRESH_COOKIE_EXPIRATION',
    );

    try {
      // Call AuthService to get access and refresh tokens
      const response: ApiResponse = await this.authService.login(
        username,
        password,
      );

      // Extract the access token and refresh token from the response
      const { accessToken, refreshToken } = response.data as {
        accessToken: string;
        refreshToken: string;
      };

      // Set the access token as an HTTP-only cookie
      res.cookie('accessToken', accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production', // Set secure flag in production
        maxAge: accessTokenExpiration, // Set the expiration time
        sameSite: 'strict',
      });

      // Set the refresh token as an HTTP-only cookie
      res.cookie('refreshToken', refreshToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        maxAge: refreshTokenExpiration,
        sameSite: 'strict',
      });

      return res.status(HttpStatus.OK).json({ message: 'Login successful' });
    } catch (error) {
      throw new GlobalException(
        error.message || 'Something went wrong',
        error.statusCode || HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
