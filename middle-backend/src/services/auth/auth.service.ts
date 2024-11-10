import { HttpService } from '@nestjs/axios';
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { ApiResponse } from 'src/interfaces/api-response.interface';
import { requestHandler } from 'src/utils/request.handler';

@Injectable()
export class AuthService {
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {}

  async login(username: string, password: string): Promise<ApiResponse> {
    const baseUrl = this.configService.get('SERVICE_BASE_URL');

    return requestHandler(
      this.httpService.post(`${baseUrl}/auth/login`, {
        username,
        password,
      }),
    );
  }

  async signup(
    username: string,
    email: string,
    password: string,
  ): Promise<ApiResponse> {
    const baseUrl = this.configService.get('SERVICE_BASE_URL');

    return requestHandler(
      this.httpService.post(`${baseUrl}/auth/signup`, {
        username,
        email,
        password,
      }),
    );
  }
}
