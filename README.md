# 💬 ChatProgram
채팅 프로토콜 설계 및 소켓을 활용한 실시간 채팅 프로그램 작성

## 🖥️ 프로젝트 소개
> **Java로 SpringBoot 의 구조 및 작동 방식을 모방한 프로젝트입니다.**
- Singleton 패턴과 DI 를 구현하여 빈 컨테이너를 모방하였습니다.
- DI 는 Proxy 패턴으로 구현하였습니다.
- 패키지를 책임에 따라 Config / Controller / Service / DAO / Domain 으로 구성했습니다.

> **채팅에 필요한 프로토콜을 직접 설계했습니다.**
- 프로토콜은 Header / Body 로 구성된 String 입니다.
- **Request Header**: URL Path, Request Method
- **Response Header**: StatusCode, isSuccess
- **Body**: Request String

## 🕰️ 개발 기간
- 23.12.02 ~ 23.12.10

## ⚙️ 개발 환경
 - `Java 11`
 - `JDK 11.0.15`
 - **IDE**: intelliJ
 - **Database**: MySQL (innoDB)

## 📌 주요 기능
 - 회원가입, 로그인, 로그아웃
 - 채팅방 조회, 생성, 초대, 나가기
 - 채팅 조회, 전송

## 🛠️ 프로토콜 구조

### Link: [Velog][velogLink]

[velogLink]: https://velog.io/@chehok/ChatProtocol-%EC%84%A4%EA%B3%84
