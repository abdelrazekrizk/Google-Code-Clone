<?php

/**
 * EpgpRaidBosses form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseEpgpRaidBossesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'      => new sfWidgetFormInputHidden(),
      'name'    => new sfWidgetFormInput(),
      'zone'    => new sfWidgetFormPropelChoice(array('model' => 'EpgpRaidZones', 'add_empty' => false)),
      'baseval' => new sfWidgetFormInput(),
    ));

    $this->setValidators(array(
      'id'      => new sfValidatorPropelChoice(array('model' => 'EpgpRaidBosses', 'column' => 'id', 'required' => false)),
      'name'    => new sfValidatorString(array('max_length' => 255)),
      'zone'    => new sfValidatorPropelChoice(array('model' => 'EpgpRaidZones', 'column' => 'id')),
      'baseval' => new sfValidatorNumber(),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorPropelUnique(array('model' => 'EpgpRaidBosses', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('epgp_raid_bosses[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'EpgpRaidBosses';
  }


}
