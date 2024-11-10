import { MiddlewareConsumer, Module, RequestMethod } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { CookieMiddleware } from './middlewares/cookie/cookie.middleware';
import { AuthController } from './controllers/auth/auth.controller';
import { AuthService } from './services/auth/auth.service';
import { HttpModule } from '@nestjs/axios';
import { LoggingService } from './services/logging/logging.service';

@Module({
  imports: [ConfigModule.forRoot(), HttpModule],
  controllers: [AuthController],
  providers: [AuthService, LoggingService],
})
export class AppModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(CookieMiddleware).forRoutes({
      path: '*',
      method: RequestMethod.ALL,
    });
  }
}
