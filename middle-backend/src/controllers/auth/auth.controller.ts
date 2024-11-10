import {
  Body,
  Controller,
  HttpStatus,
  Post,
  Query,
  Req,
  Res,
} from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Request, Response } from 'express';
import { GlobalException } from 'src/exceptions/global.exception';
import { ApiResponse } from 'src/interfaces/api-response.interface';
import { LoginPayload } from 'src/interfaces/login-payload.interface';
import { ResetPasswordPayload } from 'src/interfaces/reset-password-payload.interface';
import { SignupPayload } from 'src/interfaces/signup-payload.interface';
import { VerifyEmailPayload } from 'src/interfaces/verify-email-payload.interface';
import { AuthService } from 'src/services/auth/auth.service';
import { LoggingService } from 'src/services/logging/logging.service';
import { CustomResponse } from 'src/utils/custom-response.class';

@Controller('/api/auth')
export class AuthController {
  private readonly accessTokenExpiration: number;
  private readonly refreshTokenExpiration: number;

  constructor(
    private readonly authService: AuthService,
    private readonly configService: ConfigService,
    private readonly loggingService: LoggingService,
  ) {
    this.accessTokenExpiration = this.configService.get(
      'ACCESS_COOKIE_EXPIRATION',
    );
    this.refreshTokenExpiration = this.configService.get(
      'REFRESH_COOKIE_EXPIRATION',
    );
  }

  @Post('login')
  async login(
    @Body() loginPayload: LoginPayload,
    @Req() _req: Request,
    @Res() res: Response,
  ) {
    this.loggingService.log('Login request received');
    const { username, password } = loginPayload;
    try {
      const response: ApiResponse = await this.authService.login(
        username,
        password,
      );
      const { accessToken, refreshToken } = response.data as {
        accessToken: string;
        refreshToken: string;
      };
      res.cookie('accessToken', accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production', // Set secure flag in production
        maxAge: this.accessTokenExpiration, // Set the expiration time
        sameSite: 'strict',
      });
      res.cookie('refreshToken', refreshToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        maxAge: this.refreshTokenExpiration,
        sameSite: 'strict',
      });
      this.loggingService.log('Login successful');
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error logging in: ' + error.message);
      throw new GlobalException(
        error.message || 'Something went wrong',
        error.statusCode || HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('signup')
  async signup(@Body() signupPayload: SignupPayload, @Res() res: Response) {
    this.loggingService.log('Signup request received');
    const { username, email, password } = signupPayload;
    try {
      const response = await this.authService.signup(username, email, password);
      this.loggingService.log('Signup successful');
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error signing up: ' + error.message);
      throw error;
    }
  }

  @Post('logout')
  async logout(@Res() res: Response) {
    this.loggingService.log('Logout request received');
    try {
      res.clearCookie('accessToken', {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
      });
      res.clearCookie('refreshToken', {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
      });
      this.loggingService.log('Logout successful');
      const jsonResponse: CustomResponse = {
        message: 'Logout successful',
        statusCode: HttpStatus.OK,
      };
      return res.status(HttpStatus.OK).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error logging out: ' + error.message);
      throw error;
    }
  }

  @Post('refresh')
  async refresh(@Req() req: Request, @Res() res: Response) {
    this.loggingService.log('Refresh request received');
    try {
      const { refreshToken } = req.cookies;
      const response = await this.authService.refresh(refreshToken);
      const { accessToken } = response.data as { accessToken: string };
      res.cookie('accessToken', accessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        maxAge: this.accessTokenExpiration,
        sameSite: 'strict',
      });

      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      this.loggingService.log('Authentication token refreshed');
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error refreshing token: ' + error.message);
      throw error;
    }
  }

  @Post('forgot-password')
  async forgotPassword(@Query('email') email: string, @Res() res: Response) {
    this.loggingService.log('Forgot password request received');
    try {
      const response = await this.authService.forgotPassword(email);
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      this.loggingService.log('Forgot password successful');
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error(
        'Error sending forgot password email: ' + error.message,
      );
      throw error;
    }
  }

  @Post('reset-password')
  async resetPassword(
    @Body() resetPasswordPayload: ResetPasswordPayload,
    @Res() res: Response,
  ) {
    this.loggingService.log('Reset password request received');
    try {
      const { token, password } = resetPasswordPayload;
      const response = await this.authService.resetPassword(token, password);
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      this.loggingService.log('Password reset successful');
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error resetting password: ' + error.message);
      throw error;
    }
  }

  @Post('send-verification-email')
  async sendVerificationEmail(
    @Query('email') email: string,
    @Res() res: Response,
  ) {
    this.loggingService.log('Send verification email request received');
    try {
      const response = await this.authService.sendVerificationEmail(email);
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      this.loggingService.log('Verification email sent successfully');
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error(
        'Error sending verification email: ' + error.message,
      );
      throw error;
    }
  }

  @Post('verify-email')
  async verifyEmail(
    @Body() verifyEmailPayload: VerifyEmailPayload,
    @Res() res: Response,
  ) {
    this.loggingService.log('Verify email request received');
    try {
      const { otp, email } = verifyEmailPayload;
      const response = await this.authService.verifyEmail(otp, email);
      const jsonResponse: CustomResponse = {
        message: response.message,
        statusCode: response.statusCode,
      };
      this.loggingService.log('Email verified successfully');
      return res.status(response.statusCode).json(jsonResponse);
    } catch (error) {
      this.loggingService.error('Error verifying email: ' + error.message);
      throw error;
    }
  }
}
