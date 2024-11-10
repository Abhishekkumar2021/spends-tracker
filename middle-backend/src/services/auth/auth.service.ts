import { HttpService } from '@nestjs/axios';
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { ApiResponse } from 'src/interfaces/api-response.interface';
import { requestHandler } from 'src/utils/request.handler';

@Injectable()
export class AuthService {
  private readonly authApiUrl: string;
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {
    this.authApiUrl = this.configService.get('SERVICE_BASE_URL') + '/api/auth';
  }

  login(username: string, password: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(`${this.authApiUrl}/login`, {
        username,
        password,
      }),
    );
  }

  signup(
    username: string,
    email: string,
    password: string,
  ): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(`${this.authApiUrl}/signup`, {
        username,
        email,
        password,
      }),
    );
  }

  refresh(refreshToken: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(
        `${this.authApiUrl}/refresh?refreshToken=${refreshToken}`,
      ),
    );
  }

  forgotPassword(email: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(
        `${this.authApiUrl}/forgot-password?email=${email}`,
      ),
    );
  }

  resetPassword(token: string, password: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(`${this.authApiUrl}/reset-password`, {
        token,
        password,
      }),
    );
  }

  sendVerificationEmail(email: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(
        `${this.authApiUrl}/send-verification-email?email=${email}`,
      ),
    );
  }

  verifyEmail(otp: string, email: string): Promise<ApiResponse> {
    return requestHandler(
      this.httpService.post(`${this.authApiUrl}/verify-email`, {
        otp,
        email,
      }),
    );
  }
}
