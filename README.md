# 🌍 planetlandV2
<img width="981" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/34a18529-b761-4838-a3c2-7815f21d2491">


## 😁 소개
기존 Planetland 리팩토링 프로젝트 [링크](https://github.com/koeyhnujeel/planetland) <br />
명령어를 통해 즐기는 행성 매매 게임
<br /> <br />

## 🛠️ 개발 환경
`Java 17` `Spring boot 3.0.5` `Spring Data JPA` `Spring Security` `H2 database` `IntelliJ`
<br /> <br />

## ⌨️ 명령어 목록
`—signup` 회원가입 (하이픈 2개) <br />
`—login` 로그인 (하이픈 2개) <br />
`—logout` 로그아웃 (하이픈 2개) <br />
`mkplanet` 행성 등록 <br />
`ls -p` 행성 목록 출력 <br />
`open -p {planetId}` 행성 상세 보기 <br />
`mv -p {planetId}` 행성 수정 <br />
`rm -p {planetId}` 행성 삭제 <br />
`sell -p {planetId}` 행성 판매 등록 <br />
`buy -p {planetId}` 행성 구매 <br />
`sell cancel -p {planetId}` 행성 판매 취소 <br />
`quit` 회원가입 취소, 로그인 취소, 행성 등록 취소, 행성 수정 취소, 행성 판매 등록 취소 <br />
`close` 행성 목록 닫기
<br /> <br />

## 🧑‍💻 구현 내용
⬇️ 클릭! <br />
[`로그인/회원가입`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%EB%A1%9C%EA%B7%B8%EC%9D%B8) [`행성CRUD`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%ED%96%89%EC%84%B1crud) [`행성 거래`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%ED%96%89%EC%84%B1-%EA%B1%B0%EB%9E%98) [`행성 목록 페이징`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%ED%96%89%EC%84%B1-%EB%AA%A9%EB%A1%9D-%ED%8E%98%EC%9D%B4%EC%A7%95) [`거래 내역 페이징`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%EA%B1%B0%EB%9E%98-%EB%82%B4%EC%97%AD-%ED%8E%98%EC%9D%B4%EC%A7%95) [`테스트 코드`](https://github.com/koeyhnujeel/planetlandV2/wiki/%EA%B5%AC%ED%98%84-%EB%82%B4%EC%9A%A9-%EC%86%8C%EA%B0%9C#%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C)
<br /> <br />

## 🤔 고민한 점
Service 계층에서 Repository를 통해 조회하고 예외 처리하는 코드를 보면서, 예외 처리하는 코드까지 보일 필요가 있을까 라는 생각에서 시작하여, 비즈니스 로직을 파악하는 데 있어, 세세한 단계가 모두 보일 필요가 있을까? 라는 생각이 들었습니다. (예시는 행성 구매 입니다.) <br />
<img width="561" alt="Untitled" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/6b661fa3-2e44-4ea7-95f2-81c0fe590e99"> <br />

`그래서 저는 코드를 재구성하기로 결정했습니다. 작은 단계들을 정리하고, 큰 단계만을 강조하는 방식으로 코드를 재작성하였습니다.` <br />
`사용자와 행성 조회를 위해 UserReader, PlanetReader를 만들었고 각 Reader가 Repository를 통해 조회한 결과를 반환합니다.` <br />
`거래에 해당하는 과정은 TradeManager가 담당하게 하였고, 거래 내역 생성은 TransactionManager가 처리합니다.` <br />

![image](https://github.com/koeyhnujeel/planetlandV2/assets/125088568/326f8285-fc85-4cce-9816-e679090f0a59)<br />
서비스가 Reader와 Manager를 의존하고 이 둘이 Repository를 의존하게 됩니다. <br />

`구현 후 고민은 과연 남이 보기에도 비즈니스 로직을 한 눈에 파악하기 쉬운가?` <br />
`코드에 복잡성이 증가한 것은 아닐까?`
<br /> <br />


## 👻 트러블 슈팅
동시에 여러 사용자가 같은 행성에 대한 구매 요청을 할 경우에도 정상적으로 작동 하는지 테스트 코드를 작성해 봤습니다. 하지만 예외 발생 없이 모든 사용자와 행성 간에 거래 내역이 생성되는 `동시성 문제`가 발생하였습니다. <br />

<img width="870" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/64fe9477-b672-431b-ad13-c40c72213741"> <br />
<img width="870" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/179149a6-fd20-4a77-8b5e-75c39900476e"> <br />
<img width="870" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/35779d11-6437-47bf-8613-edf302dd7a6d"> <br />

<img width="870" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/d9531dc3-c9c2-4108-8a92-1d845af53cdd"> <br />

`동시에 수정하는 일이 빈번하게 발생하는가?` <br />
문제를 해결하기 위해 찾아보던 중, 저는 낙관적 락과 비관적 락을 알게 되었습니다. 어떤 락을 선택할지 고려해야 할 점은 '동시에 수정하는 일이 빈번하게 발생하는가?'였습니다. 낙관적 락은 충돌이 발생했을 때 롤백하고 재시도하는 비용이 들지만, 비관적 락은 처음부터 충돌을 방지하여 이러한 비용을 줄일 수 있습니다. 저는 동시에 행성 구매를 요청하는 경우가 많을 것이라고 판단하였고, 따라서 `@Lock` 어노테이션을 통해 `비관적 락`을 적용한 새로운 행성 조회 메서드를 Repository에 추가하여, 이를 통해 동시성 문제를 해결하였습니다. <br />

<img width="870" alt="image" src="https://github.com/koeyhnujeel/planetlandV2/assets/125088568/f16cacd5-21aa-49eb-8ce0-d6ff8b5ca7cb"> <br />





