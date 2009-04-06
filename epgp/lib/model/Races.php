<?php

class Races extends BaseRaces
{
  public function __toString(){
    return $this->name;
  }
}
