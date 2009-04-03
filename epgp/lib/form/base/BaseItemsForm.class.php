<?php

/**
 * Items form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseItemsForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'          => new sfWidgetFormInputHidden(),
      'name'        => new sfWidgetFormInput(),
      'playerid'    => new sfWidgetFormPropelChoice(array('model' => 'Roster', 'add_empty' => false)),
      'raidid'      => new sfWidgetFormPropelChoice(array('model' => 'Raids', 'add_empty' => false)),
      'itemid'      => new sfWidgetFormInput(),
      'baseval'     => new sfWidgetFormInput(),
      'inflatedval' => new sfWidgetFormInput(),
    ));

    $this->setValidators(array(
      'id'          => new sfValidatorPropelChoice(array('model' => 'Items', 'column' => 'id', 'required' => false)),
      'name'        => new sfValidatorString(array('max_length' => 255)),
      'playerid'    => new sfValidatorPropelChoice(array('model' => 'Roster', 'column' => 'id')),
      'raidid'      => new sfValidatorPropelChoice(array('model' => 'Raids', 'column' => 'id')),
      'itemid'      => new sfValidatorInteger(),
      'baseval'     => new sfValidatorNumber(),
      'inflatedval' => new sfValidatorNumber(),
    ));

    $this->widgetSchema->setNameFormat('items[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Items';
  }


}
