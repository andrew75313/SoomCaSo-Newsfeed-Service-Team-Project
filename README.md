# 📰Newsfeed 관리 웹 서버(숨.카.소)
<p align="center"><img src="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fcdeec01c-ee3c-4ff1-a6c5-a9bbb63443fb%2Faf62e421-cee9-42e0-9845-189429922963.png?table=block&id=d5fd014c-55b7-46d3-bad8-cfbd4b5daf51&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=81832d12-bc15-4ae9-a090-4b1b1ca1bbe6&cache=v2" width=400></p>

이 서버는 지구 곳곳에 숨겨진 카페에 대한 정보를 포스팅하고 공유할 수 있는 Newsfeed 공간을 서비스하기 위해 개발된 웹 서버입니다.<br/>

이 서버는 누구나 게시물들을 조회할 수 있고, 회원가입과 Email 인증을 완료한 사용자에 한하여 로그인을 통해 게시물과 댓글, 좋아요를 등록, 수정, 삭제할 수 있습니다.

이 서버는 IntelliJ, Java, SpringBoot를 사용하여 개발되었으며 백엔드 데이터베이스로는 AWS RDS(MySQL)과 Redis를 사용합니다.

각 기능은 RESTful API를 통해 구현되었으며, Client는 HTTP 요청을 통해 서버와 상호작용합니다.

데이터의 보안을 위해 인증된 사용자만 접근할 수 있도록 JWT를 사용하여 API 요청마다 사용자를 검증하고 본 서비스의 사용자일 경우에만 접근을 허용합니다.

<br/>

# 📜 Table
- [Team](#-Team)
- [Tech Stack](#-Tech-Stack)
- [Feature](#-Feature)
- [Wireframe](#-Wireframe)
- [ERD](#-ERD)
- [API Document](#-API-Document)
- [Code Convention](#-Code-Convention)
- [Git Rules](#-Git-Rules)

<br/>

## 🤝 Team
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/ggumi030"><img src="https://avatars.githubusercontent.com/u/130031828?v=4" width="100px;" alt=""/><br /><sub><b> 팀장 : 권수연 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/seungsuuu"><img src="https://avatars.githubusercontent.com/u/48900537?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 김승수 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/andrew75313"><img src="https://avatars.githubusercontent.com/u/161192870?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 김현진 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/Berithx"><img src="https://avatars.githubusercontent.com/u/154594004?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 이유환 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/hongsy521"><img src="https://avatars.githubusercontent.com/u/124027140?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 홍서영 </b></sub></a><br /></td>
    </tr>
  </tbody>
</table>
<br/>

<details>
<summary><big>권수연</big></summary>
<div markdown="1">

- Security Filter
    - AuthenticationFilter 설계 및 구현
    - AuthorizationFilter 설계 및 구현
- Feed 좋아요 API
    - Feed 좋아요 증감 API 구현
- Exception Handler Design
    - Exception Handling Response 일원화
    - Filter Exception 처리
    - Global Exception 처리
</div>
</details>

<details>
<summary><big>김승수</big></summary>
<div markdown="1">

- JwtProvider
    - JWT(AT, RT) 생성, 검증 및 재발행 클래스 설계 및 구현
- Comment
    - Comment Entity 설계 및 연관관계 설정
    - Comment CRUD API 구현
</div>
</details>

<details>
<summary><big>김현진</big></summary>
<div markdown="1">

- Wireframe Design
- Feed
    - Feed Entity 설계 및 구현
    - Feed CRUD API 구현
    - Pagination 처리
    - Feed 생성일자/좋아요 기준 정렬 조회
    - 기간별 조회
</div>
</details>

<details>
<summary><big>이유환</big></summary>
<div markdown="1">

- Redis 환경 구축
- Security
    - WebSecurity 설계 및 구현
    - UserDetails 구현
    - User 로그아웃 API 구현
- Email Authentication
    - 회원가입 간 사용된 Email 활용, 인증번호 발송 API 구현
    - 발송된 인증번호 검증 및 User 상태 전환 API 구현
</div>
</details>

<details>
<summary><big>홍서영</big></summary>
<div markdown="1">

- User
    - User Entity 설계 및 구현
    - 회원가입 API 구현
    - 회원탈퇴 API 구현
    - User 프로필 조회 API 구현
    - User 프로필 수정 API 구현
- Comment 좋아요
    - Comment 좋아요 증감 API 구현
</div>
</details>

[(Back to top)](#-table)

<br/>

## 🤖 Tech Stack
|     Type     |                                                                                                                                                                                               Tech                                                                                                                                                                                                |        Version        |                                    Comment                                     |
|:------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------:|:------------------------------------------------------------------------------:|
| IDE / EDITOR |                                                                                                                                   ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)                                                                                                                                   |           -           |                                       -                                        |
|  Framework   |                                                                                                                                         ![Spring](https://img.shields.io/badge/springBoot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)                                                                                                                                          |         3.1.0         |                                       -                                        |
|   Language   |                                                                                                                                             ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)                                                                                                                                             |        JDK 17         |                                       -                                        |
|     IaaS     |                                                                                                                        ![AWS EC2](https://img.shields.io/badge/AWS_EC2-RDS?style=for-the-badge&logo=amazonec2&logoColor=white&logoSize=amg&labelColor=FF9900&color=FF9900)                                                                                                                        | Amazon Linux 2023 AMI |                                       -                                        |
|   Database   |                                                                  ![AWS RDS](https://img.shields.io/badge/AWS_RDS-RDS?style=for-the-badge&logo=amazonrds&logoColor=white&logoSize=amg&labelColor=527FFF&color=527FFF) <br> ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)                                                                  |   MySQL ver.8.0.35    |                                      Main                                      |
|   Database   |                                                                                                                                             ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                             |         7.2.5         |                             Used On EC2, <br> Sub                              |
|    Record    |                                                        ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)                                                                                                                                                                                                                               |           -           | [Link](https://teamsparta.notion.site/9-9999-c6dca1e35cec4e9086fccd3d0d624176) |

[(Back to top)](#-table)

<br/>

## 🚀 Feature

- 조회를 제외한 모든 기능은 회원가입 및 Email 인증, 로그인을 통해 인증이 완료되어야만 접근할 수 있습니다.
- 이를 위해 JWT를 사용하였고 정상 로그인 처리가 되면 서버는 AccessToken(이하 AT)과 RefreshToken(이하 RT)를 발행하여 Header를 통해 반환하고 Client는 2종의 토큰을 사용하여 서버에 접근해야 정상 처리됩니다.
- 또한, 서버의 보안을 위하여 AT의 유효시간을 짧게 설정하고 사용자가 정상적으로 서버에 API를 요청할 때마다 새로운 유효시간을 가진 토큰을 재발행하여 서비스를 지속 이용할 수 있으며, AT가 공격자에 의해 탈취된 경우를 대비합니다.
- AWS RDS와 Redis는 각각 DB의 역할을 분리하여 사용자 정보, 게시물 정보 등 장기적인 유지가 필요한 데이터는 RDS에 저장되고, Email 인증번호 등 짧은 시간만 지속되어야하는 데이터를 저장/관리합니다.

<br/>

### 1. User 기능
    - 회원가입
       - 아이디, 비밀번호, 이름, 이메일, 한줄 소개를 입력하여 회원가입할 수 있으며, 사용자 정보는 RDS에 저장됩니다.
       - 회원가입 후 사용자는 "UNTHORIZED" 상태이며, 이메일 인증을 통해 서비스를 이용할 수 있는 "ACTIVATE" 상태로 전환됩니다.
       - 이메일 인증은 회원가입 간 입력한 이메일로 6자리의 숫자의 인증번호가 발송됩니다.
       - 해당 인증번호는 Redis에 저장되어 3분 동안만 유효하며, 시간 내 인증번호 검증 성공 시 "ACTIVATE" 상태로 전환됩니다.
    - 회원탈퇴
       - 사용자는 본인의 선택에 따라 회원탈퇴를 할 수 있습니다.
       - 회원탈퇴를 위해서는 로그인된 상태이어야하며, AT가 유효한 상태이어야 합니다.
       - 회원탈퇴 간 사용자 검증을 위해 아이디와 비밀번호를 입력을 필요로 합니다.
       - JWT에 등록된 사용자 정보와 검증을 위해 입력된 아이디, 비밀번호를 확인하여 회원탈퇴 처리가 됩니다.
       - 사용자 정보가 DB에서 삭제되지 않고 "ACTIVATE" 상태에서 "DEACTIVATE" 상태로 전환되어 차후 통계 등에 활용할 수 있습니다.
    - 로그인
       - 사용자는 아이디, 비밀번호를 사용하여 로그인할 수 있습니다.
       - 로그인은 DB에 존재하고 "ACTIVATE" 상태인 사용자만 로그인이 정상적으로 처리됩니다.
       - 로그인이 정상적으로 처리되면 서버는 JWT(AT, RT)를 발행하여 Header를 통해 Client에 반환합니다.
    - 로그아웃
       - 로그인 상태인 사용자는 JWT를 통하여 로그아웃할 수 있습니다.
       - Client가 서버에 로그아웃을 요청하면 JWT의 사용자 정보와 로그아웃 시도 사용자의 일치여부를 확인합니다.
       - JWT의 사용자 정보와 사용자 정보가 일치하면 DB에 저장된 RT를 삭제하고 HTTP 상태코드 204를 클라이언트에 반환합니다.
       - 반환된 상태코드를 사용하여 프론트는 AT와 RT를 삭제합니다.
    - 프로필 조회
       - 로그인을 하지 않은 상태에서도 타 사용자의 프로필을 조회할 수 있습니다.
       - 프로필 조회 시 해당 사용자의 아이디, 이름, 이메일, 한줄 소개를 반환합니다.
    - 프로필 수정
       - 로그인 상태인 사용자는 본인의 프로필을 수정할 수 있습니다.
       - 프로필 수정은 비밀번호, 이름, 한줄 소개만 수정할 수 있습니다.
       - 프로필 수정 후 민감정보가 제외하여 수정된 프로필을 반환하여 수정 결과를 확인할 수 있습니다.
### 2. Feed 기능
    - Feed 등록
       - 로그인된 사용자는 내용을 입력하여 Feed를 등록할 수 있습니다.
       - 등록된 Feed는 ID, 사용자 아이디, 내용, 좋아요 수, 작성일, 수정일, 댓글을 포함하고 사용자에게 반환되어 등록 결과를 확인할 수 있습니다.
    - Feed 단건 조회
       - 로그인을 하지 않은 상태에서도 등록되어있는 Feed를 조회할 수 있습니다.
       - 단건 조회 시 Feed ID, 작성자 아이디, 내용, 좋아요 수, 해당 피드에 작성된 댓글들을 반환합니다.
    - Feed 전체 조회
       - 로그인을 하지 않은 상태에서도 등록되어있는 Feed를 조회할 수 있습니다.
       - 전체 조회 시 Pagination을 지원하며 생성일자, 좋아요 수로 정렬하여 전체 조회할 수 있습니다.
       - 필요 시 날짜를 범위적으로 선택하여 해당 일정에 포함되는 생성일자의 Feed를 전체 조회할 수 있습니다.
       - Pagination은 1page당 10개의 Feed를 제공하고, Feed ID, 작성자 아이디, 내용, 좋아요 수 등을 반환합니다.
    - Feed 수정
       - 로그인된 사용자는 본인이 작성한 Feed에 한하여 내용을 수정할 수 있습니다.
       - 수정은 내용만 가능합니다.
       - AT를 사용하여 인가 처리된 사용자에 한하여 수정을 진행할 수 있습니다.
       - 수정 후 수정된 Feed를 반환하여 사용자는 결과를 확인할 수 있습니다.
    - Feed 삭제
       - 로그인된 사용자는 본인이 작성한 Feed에 한하여 삭제할 수 있습니다.
       - AT를 사용하여 인가 처리된 사용자에 한하여 삭제를 진행할 수 있습니다.
       - Feed를 삭제할 경우 해당 Feed와 연결된 Comment와 Like 데이터가 함께 삭제됩니다.
       - 삭제 처리 완료 후 서버는 Client에게 처리 완료 메세지를 반환합니다.
### 3. Comment 기능
    - Comment 등록
       - 로그인된 사용자는 내용을 입력하여 Feed에 Comment를 등록할 수 있습니다.
       - 등록된 Comment는 ID, 내용, 작성일, 수정일, 작성자 이름, Feed ID를 포함하고 사용자에게 반환되어 등록 결과를 확인할 수 있습니다.
    - Comment 조회
       - 로그인을 하지 않은 상태에서도 등록되어있는 Feed를 조회할 수 있습니다.
       - 조회하고자 하는 Feed의 ID와 Comment ID를 입력받아 해당 Comment를 조회합니다.
       - 조회된 Comment는 ID, 내용, 작성일, 수정일, 작성자 이름, Feed ID를 포함하고 사용자에게 반환됩니다.
    - Comment 수정
       - 로그인된 사용자는 본인이 작성한 Comment에 한하여 수정할 수 있습니다.
       - 수정은 내용만 가능합니다.
       - AT를 사용하여 인가 처리된 사용자에 한하여 수정을 진행할 수 있습니다.
       - 수정 후 수정된 Comment를 반환하여 사용자는 결과를 확인할 수 있습니다.
    - Comment 삭제
       - 로그인된 사용자는 본인이 작성한 Comment에 한하여 삭제할 수 있습니다.
       - AT를 사용하여 인가 처리된 사용자에 한하여 삭제을 진행할 수 있습니다.
       - Comment를 삭제한 경우 해당 Comment와 연결된 Like 데이터가 함께 삭제됩니다.
       - 삭제 처리 완료 후 서버는 삭제된 Comment의 ID와 처리 완료 메세지를 반환합니다.
### 4. Like 기능
    - Feed Like 증감
       - 로그인된 사용자는 Feed의 Like를 증감할 수 있습니다.
       - Like를 요청하는 Feed에 해당 사용자의 Like 데이터가 존재하지 않으면 증가되고, 존재하면 감소됩니다.
    - Comment Like 증감
       - 로그인된 사용자는 Comment의 Like를 증감할 수 있습니다.
       - Like를 요청하는 Comment에 해당 사용자의 Like 데이터가 존재하지 않으면 증가되고, 존재하면 감소됩니다.

[(Back to top)](#-table)

<br/>

## ✍ Wireframe
<img src="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fa5fa57d2-be4e-40a6-aded-c8c405d4cab5%2Fwireframe.png?table=block&id=1a46c55e-1bf5-42fe-969f-93c491cb464b&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1920&userId=81832d12-bc15-4ae9-a090-4b1b1ca1bbe6&cache=v2">

[(Back to top)](#-table)

<br/>

## 🔗 ERD
<img src="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F853033e2-a62d-497c-8568-7fb91c03418b%2FSpring_%25EC%2588%2599%25EB%25A0%25A8%25EC%25A3%25BC%25EC%25B0%25A8_9%25EC%25A1%25B0_%25EB%2589%25B4%25EC%258A%25A4%25ED%2594%25BC%25EB%2593%259C_%25ED%2594%2584%25EB%25A1%259C%25EC%25A0%259D%25ED%258A%25B8_ERD.drawio.png?table=block&id=88819fcb-1354-4814-8c0f-21fe51b40197&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1370&userId=81832d12-bc15-4ae9-a090-4b1b1ca1bbe6&cache=v2">

[(Back to top)](#-table)

<br/>

## 💥 API Document
<img src="https://github.com/ggumi030/Newsfeed-Team-Project/assets/154594004/747a9bd9-f8ee-453c-94e6-24b726d0c266">

[(Back to top)](#-table)

<br/>

## ⚖️ Code Convention
<img src="https://github.com/ggumi030/Newsfeed-Team-Project/assets/154594004/8fca7dad-c132-4a09-ad75-996536f1d069">

[(Back to top)](#-table)

<br/>

## 📏 Git Rules
<img src="https://github.com/ggumi030/Newsfeed-Team-Project/assets/154594004/c8ec6534-a5b4-4766-966d-0ee77f9cf639">
