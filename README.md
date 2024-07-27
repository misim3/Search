# Search

## 프로젝트 설명
이 프로젝트는 사용자들간의 중간 지점을 탐색하는 기능을 구현한 백엔드 시스템입니다. 모임 계획 과정의 간소화를 목적으로 한 프로젝트에서 사용하는 기능입니다.
현재 서버는 구동을 멈춘 상태입니다.
<br/>

## 사용 기술 스택
Java 17, spring boot, spring data jpa, MySQL, AWS EC2
<br/>

## 서버 구성도
![캡디아키텍처 drawio](https://github.com/user-attachments/assets/69ce3992-6f9d-46aa-a17b-feb0b4fb7b5f)
<br/>

## [ERD](https://github.com/misim3/Search/wiki/ERD)

## 주요 기능
 - **중간 지점 탐색:**
   - 모든 모임원의 출발 장소에서 비슷한 이동 시간이 소요되는 노드 탐색
 - **중간 지점 선별:**
   - 탐색 결과 노드들을 군집화하여 모임 지점 선별

#### 중간 지점 탐색 기준: 출발 장소부터 모임 지역까지의 이동 시간
#### 중간 지점 선별 기준: 접근성과 상권 발달의 상관 관계 고려하여 번화가 인근
<br/>

## [비즈니스 로직](https://github.com/misim3/Search/wiki/%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4-%EB%A1%9C%EC%A7%81)
